package com.aquima.plugin.webfileupload;

import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * A wrap for the MultipartHttpServletRequest
 * 
 * @author b.van.de.ven
 * @since 9.2
 */
public class MultipartHttpRequest {

  private final MultipartHttpServletRequest request;

  /**
   * Constructs the MultipartHttpRequest
   * 
   * @param request the {@link MultipartHttpServletRequest}
   */
  public MultipartHttpRequest(MultipartHttpServletRequest request) {
    this.request = request;
  }

  /**
   * @return the element name
   */
  public String getElementName() {
    return this.request.getParameter("elementName");
  }

  /**
   * Get the file by fileReference
   * 
   * @param fileRef The file reference
   * @return the requestFile
   */
  public RequestFile getFile(String fileRef) {
    return new RequestFile(this.request.getFile(fileRef));
  }

}
