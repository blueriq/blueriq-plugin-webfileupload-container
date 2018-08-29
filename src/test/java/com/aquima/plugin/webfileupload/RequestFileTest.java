package com.aquima.plugin.webfileupload;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class RequestFileTest extends RequestFile {

  public RequestFileTest(MultipartFile file) {
    super(file);
  }

  public RequestFileTest(String name, byte[] content) {
    super(new MockMultipartFile(name, content));

  }
}
