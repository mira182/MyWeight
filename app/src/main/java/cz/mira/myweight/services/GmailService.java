package cz.mira.myweight.services;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.common.collect.Lists;

import org.mortbay.log.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class GmailService {
    private static final String APPLICATION_NAME = "MyWeight";

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final String USER = "me";

    private static final List<String> SCOPES = Lists.newArrayList(
            GmailScopes.GMAIL_READONLY,
            GmailScopes.GMAIL_MODIFY,
            GmailScopes.MAIL_GOOGLE_COM,
            GmailScopes.GMAIL_METADATA);

    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static final String QUERY = "from:apps@tanita.eu";

    public byte[] getAttachmentFromMailByQuery() throws IOException, GeneralSecurityException {
        return listMessagesMatchingQuery(getGmailService(), USER, QUERY);
    }

    public boolean doesNewTanitaEmailExist() throws IOException, GeneralSecurityException {
        final WeightLastUpdate weightLastUpdate = weightLastUpdateRepository.findTopByOrderByIdDesc();
        final String query = QUERY +
                " after:" +
                weightLastUpdate.getUpdated().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));

        Log.debug("Checking if there is a new email from tanita with query {}...", query);

        final ListMessagesResponse response = getGmailService()
                .users()
                .messages()
                .list(USER)
                .setMaxResults(1L)
                .setQ(query)
                .execute();

        final List<Message> foundMessages = response.getMessages();
        if (foundMessages == null) {
            Log.debug("No new messages found.");
            return false;
        } else {
            Log.debug("Found {} messages", foundMessages.size());
            return true;
        }
    }

    private Gmail getGmailService() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private byte[] listMessagesMatchingQuery(Gmail service, String userId,
                                             String query) throws IOException {
        Log.debug("Getting messages from gmail with query {}", query);

        final ListMessagesResponse response = service.users()
                .messages()
                .list(userId)
                .setMaxResults(1L)
                .setQ(query)
                .execute();
        final List<Message> messages = response.getMessages();

        Log.debug("Got {} messages from gmail: {}", messages.size(), messages);

        if (response.getMessages() == null || response.getMessages().isEmpty()) {
            throw new IOException("No messages found in Gmail");
        }

        return getAttachments(service, userId, response.getMessages().get(0).getId());
    }

    /**
     * Get the attachments in a given email.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me"
     * can be used to indicate the authenticated user.
     * @param messageId ID of Message containing attachment..
     * @throws IOException when it failes to read email attachment
     */
    private byte[] getAttachments(Gmail service, String userId, String messageId) throws IOException {
        final Message message = service.users().messages().get(userId, messageId).execute();
        final List<MessagePart> parts = message.getPayload().getParts();
        for (MessagePart part : parts) {
            if (part.getFilename() != null && part.getFilename().length() > 0) {
                String filename = part.getFilename();
                String attId = part.getBody().getAttachmentId();
                MessagePartBody attachPart = service.users().messages().attachments().
                        get(userId, messageId, attId).execute();

                Log.debug("Saving attachment with name {}", filename);

                return Base64.decodeBase64(attachPart.getData());
            }
        }
        return null;
    }

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GmailService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
