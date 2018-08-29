package com.aquima.plugin.webfileupload;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * A wrap for the MultipartFile
 * 
 * @author b.van.de.ven
 * @since 9.2
 */
public class RequestFile {

  private final MultipartFile file;

  /**
   * Constructs the RequestFile
   * 
   * @param file
   */
  public RequestFile(MultipartFile file) {
    this.file = file;
  }

  /**
   * @return the name
   */
  public String getName() {
    return this.file.getName();
  }

  /**
   * @return the original file name
   */
  public String getOriginalFilename() {
    return this.file.getOriginalFilename();
  }

  /**
   * @return the content type
   */
  public String getContentType() {
    return this.file.getContentType();
  }

  /**
   * @return if file is empty
   */
  public boolean isEmpty() {
    return (this.file == null || this.file.isEmpty());
  }

  /**
   * @return the size
   */
  public long getSize() {
    return this.file.getSize();
  }

  /**
   * @return the file data
   */
  public byte[] getBytes() throws IOException {
    return this.file.getBytes();
  }

  /**
   * @return the inputstream
   */
  public InputStream getInputStream() throws IOException {
    return this.file.getInputStream();
  }

  /**
   * Transfers the file to destination
   * 
   * @param dest the destination
   */
  public void transferTo(File dest) throws IOException, IllegalStateException {
    this.file.transferTo(dest);
  }
}
