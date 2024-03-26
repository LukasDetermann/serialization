package io.determann.serialization.view;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.shadow.AnnotationUsage;

import java.util.Optional;

public interface SettingsRenderer
{
   public String namespace();

   public Optional<String> render(AnnotationUsage annotationUsage, Annotationable annotated);
}
