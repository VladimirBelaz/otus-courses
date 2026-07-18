package ru.otus.helpers;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPMessage;

public class SoapHelper {

    public SOAPMessage call(String url, SOAPMessage request) {

        try {

            SOAPConnectionFactory connectionFactory =
                    SOAPConnectionFactory.newInstance();

            SOAPConnection connection =
                    connectionFactory.createConnection();

            SOAPMessage response =
                    connection.call(request, url);

            connection.close();

            return response;

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    public SOAPMessage createEmptyMessage() {

        try {

            MessageFactory messageFactory =
                    MessageFactory.newInstance();

            return messageFactory.createMessage();

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    public SOAPBody getBody(SOAPMessage message) {

        try {

            return message.getSOAPBody();

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

}