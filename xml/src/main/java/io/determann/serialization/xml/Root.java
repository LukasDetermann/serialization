package io.determann.serialization.xml;

import io.determann.serialization.view.RenderableSetting;

import static io.determann.serialization.xml.Xml.NAMESPACE;

@RenderableSetting(namespace = NAMESPACE)
public @interface Root
{
   String value();
}
