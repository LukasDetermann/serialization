import io.determann.serialization.view.SettingsRenderer;
import io.determann.serialization.xml.XmlSettingsRenderer;

module serialization.xml {
   requires serialization.view;
   requires shadow.core;
   requires java.xml;

   provides SettingsRenderer with XmlSettingsRenderer;
}