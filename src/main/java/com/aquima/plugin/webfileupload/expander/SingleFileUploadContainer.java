package com.aquima.plugin.webfileupload.expander;

import com.aquima.interactions.composer.model.Button;
import com.aquima.interactions.composer.model.Container;
import com.aquima.interactions.composer.model.ContentStyle;
import com.aquima.interactions.composer.model.Element;
import com.aquima.interactions.composer.model.definition.ContainerDefinition;
import com.aquima.interactions.foundation.exception.AppException;
import com.aquima.interactions.foundation.exception.InvalidStateException;
import com.aquima.interactions.foundation.text.MultilingualText;
import com.aquima.interactions.foundation.text.StringUtil;
import com.aquima.interactions.metamodel.exception.UnknownEntityException;
import com.aquima.interactions.portal.IContainerContext;
import com.aquima.interactions.portal.IContainerExpander;
import com.aquima.interactions.profile.exception.CreateInstanceException;
import com.aquima.interactions.profile.exception.UnknownInstanceException;
import com.aquima.plugin.webfileupload.FileUploadHandler;

import com.blueriq.component.api.annotation.AquimaExpander;

/**
 * Dynamic container in which a file can be uploaded.
 * 
 * @author b.van.de.ven
 * @since 9.2
 */
@AquimaExpander("AQ_Web_FileUpload")
public class SingleFileUploadContainer implements IContainerExpander {

  private static final ContentStyle PRESENTATION_STYLE_UPLOAD = ContentStyle.valueOf("webfile_upload");

  /**
   * Adds a dynamic container that enables uploading files.
   */
  public SingleFileUploadContainer() {}

  @Override
  public Container expand(Container container, ContainerDefinition containerDefinition, IContainerContext context)
      throws Exception {

    String fullAttributeName = context.getParameter("FileName");

    if (StringUtil.isEmpty(fullAttributeName)) {
      throw new InvalidStateException("Missing parameter 'FileName'.");
    }

    String successTextItem = context.getParameter("SuccessText");
    if (StringUtil.isEmpty(successTextItem)) {
      throw new InvalidStateException("Missing parameter 'SuccessText'.");
    }

    final boolean isUploaded = this.isUploaded(context);

    final Element element = new Button(fullAttributeName, new MultilingualText("Upload"));
    container.addElement(element);
    if (isUploaded) {
      container.addElement(context.getElementComposer().expandText(successTextItem));
    }

    if (container.getContentStyle().equals(ContainerDefinition.DEFAULT_CONTAINER_CONTENT_STYLE)) {
      container.setContentStyle(PRESENTATION_STYLE_UPLOAD);
    }

    return container;
  }

  private boolean isUploaded(IContainerContext context)
      throws AppException, UnknownInstanceException, UnknownEntityException, CreateInstanceException {
    return context.getPageScope().hasAttribute(FileUploadHandler.FILE_UPLOADED);
  }
}
