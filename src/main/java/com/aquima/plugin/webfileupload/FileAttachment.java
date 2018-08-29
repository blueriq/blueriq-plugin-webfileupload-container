package com.aquima.plugin.webfileupload;

/**
 * The file that will be uploaded
 * 
 * @author b.van.de.ven
 * @since 9.2
 */
public class FileAttachment {

  private final String fileReference;
  private final RequestFile file;

  /**
   * Constructs FileAttachment
   * 
   * @param fileReference the file reference
   * @param file the {@RequestFile}
   */
  public FileAttachment(String fileReference, RequestFile file) {
    this.fileReference = fileReference;
    this.file = file;
  }

  public String getValueReference() {
    return this.fileReference;
  }

  public RequestFile getFile() {
    return this.file;
  }
}
