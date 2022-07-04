package org.ujar.micro.k8s.bookingdb.bookingcomclient.exception;

public class NetClientCommunicationException extends RuntimeException {
  public NetClientCommunicationException(Throwable cause) {
    super(cause);
  }
}
