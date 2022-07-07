package org.ujar.micro.k8s.bookingdb.apiclient.exception;

public class NetClientMisconfigurationException extends IllegalStateException {
  public NetClientMisconfigurationException(Throwable cause) {
    super("Invalid configuration in loremipsum.net-client properties.", cause);
  }
}
