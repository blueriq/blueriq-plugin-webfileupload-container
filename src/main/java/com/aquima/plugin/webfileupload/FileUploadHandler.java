package com.aquima.plugin.webfileupload;

import com.aquima.interactions.foundation.exception.InvalidStateException;
import com.aquima.interactions.foundation.logging.LogFactory;
import com.aquima.interactions.foundation.logging.Logger;
import com.aquima.interactions.foundation.types.StringValue;
import com.aquima.interactions.framework.FlowScopeFile;
import com.aquima.interactions.framework.ScopeKeys;
import com.aquima.interactions.metamodel.AttributeReference;
import com.aquima.interactions.metamodel.exception.UnknownAttributeException;
import com.aquima.interactions.metamodel.exception.UnknownEntityException;
import com.aquima.interactions.portal.IActionContext;
import com.aquima.interactions.portal.IActionHandler;
import com.aquima.interactions.portal.IActionResult;
import com.aquima.interactions.portal.PortalException;
import com.aquima.interactions.profile.IEntityInstance;
import com.aquima.plugin.webfileupload.actionhandler.EmptyActionResult;
import com.aquima.plugin.webfileupload.controller.PortalSessionWrap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Uploads the file information and its content in the flowscope.
 * 
 * @author b.van.de.ven
 * @since 9.2
 */
public class FileUploadHandler {

  private static final Logger LOG = LogFactory.getLogger(FileUploadHandler.class);
  public static final String FILE_UPLOADED = "isFileUploaded";

  /**
   * Checks the multipart request for uploaded files and handles the uploaded files.
   * 
   * @param session the current PortalSession
   * 
   * @param request the http request
   */
  public void handleUploads(PortalSessionWrap session, MultipartHttpRequest request) {
    List<FileAttachment> attachments = new ArrayList<FileAttachment>();

    String fileRef = request.getElementName();
    RequestFile file = request.getFile("file");
    if (!file.isEmpty()) {
      attachments.add(new FileAttachment(fileRef, file));
    }
    saveFiles(session, attachments);
  }

  /**
   * Checks the multipart request for uploaded files and handles the uploaded files.
   * 
   * @param session the current PortalSession
   * 
   * @param attachments the files to upload
   */
  public static void saveFiles(PortalSessionWrap session, List<FileAttachment> attachments) {
    session.executeInlineAction(new SaveFilesHandler(attachments), null);
  }

  private static FlowScopeFile toAttributeValue(RequestFile file) throws IOException {
    return new FlowScopeFile(file.getOriginalFilename(), file.getContentType(), file.getBytes());
  }

  private static class SaveFilesHandler implements IActionHandler {

    private final List<FileAttachment> attachments;

    public SaveFilesHandler(List<FileAttachment> attachments) {
      super();
      this.attachments = attachments;
    }

    @Override
    public IActionResult handle(IActionContext context) throws PortalException, Exception {
      for (FileAttachment attachment : this.attachments) {
        String fileRef = attachment.getValueReference();

        LOG.info("Saving " + attachment.getFile().getOriginalFilename());

        context.getFlowScope().setAttribute(ScopeKeys.FILE_KEY,
            FileUploadHandler.toAttributeValue(attachment.getFile()));

        AttributeReference attributeReference = new AttributeReference(fileRef);
        try {
          context.getMetaModel().getEntityDefinition(attributeReference.getEntityName())
              .getAttribute(attributeReference.getAttributeName());
        } catch (UnknownEntityException error) {
          throw new InvalidStateException("Invalid Entity in parameter 'FileName' " + attributeReference.getFullname());
        } catch (UnknownAttributeException error) {
          throw new InvalidStateException(
              "Invalid Attribute in parameter 'FileName' " + attributeReference.getFullname());
        }

        IEntityInstance entityInstance =
            context.getProfile().getSingletonInstance(attributeReference.getEntityName(), true);
        entityInstance.setValue(attributeReference.getAttributeName(),
            new StringValue(attachment.getFile().getOriginalFilename()));

        context.getPageScope().setAttribute(FILE_UPLOADED, true);
      }

      return new EmptyActionResult();
    }

    @Override
    public boolean isReadOnly() {
      return false;
    }

  }
}
