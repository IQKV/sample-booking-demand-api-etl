package org.ujar.micro.k8s.bookingdb.bookingcomclient.exception;

public class NetClientMisconfigurationException extends IllegalStateException {
  public NetClientMisconfigurationException(Throwable cause) {
    super("Invalid configuration in loremipsum.net-client properties.", cause);
  }
}
