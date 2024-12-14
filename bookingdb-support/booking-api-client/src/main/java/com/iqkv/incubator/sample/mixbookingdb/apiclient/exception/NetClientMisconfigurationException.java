package com.iqkv.incubator.sample.mixbookingdb.apiclient.exception;

public class NetClientMisconfigurationException extends IllegalStateException {
  public NetClientMisconfigurationException(Throwable cause) {
    super("Invalid configuration in lorem.net-client properties.", cause);
  }
}
