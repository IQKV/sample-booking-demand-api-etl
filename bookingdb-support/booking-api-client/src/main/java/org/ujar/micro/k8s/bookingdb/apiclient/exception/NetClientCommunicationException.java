package org.ujar.micro.k8s.bookingdb.apiclient.exception;

public class NetClientCommunicationException extends RuntimeException {
  public NetClientCommunicationException(Throwable cause) {
    super(cause);
  }
}
