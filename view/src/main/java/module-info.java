import io.determann.serialization.view.SettingsRenderer;

module serialization.view {
   requires shadow.core;
   requires shadow.lang.model;
   requires shadow.annotation.processing;
   requires java.compiler;

   exports io.determann.serialization.view;

   uses SettingsRenderer;
}