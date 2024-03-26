package io.determann.serialization.view;

import java.util.List;
import java.util.Optional;

public sealed interface Property<TYPE, PROPERTY, SELF extends Property<TYPE, PROPERTY, SELF>> permits SerializationProperty,
                                                                                                      DeSerializationProperty
{
   String getName();

   Class<PROPERTY> getType();

   SELF withSetting(PropertySetting setting);

   List<PropertySetting> getSettings();

   default Optional<PropertySetting> getSetting(String namespace, String name)
   {
      return getSettings().stream()
                          .filter(setting -> setting.getName().equals(name))
                          .findAny();
   }

   default PropertySetting getSetting(String namespace, String name, Object defaultValue)
   {
      return getSettings().stream()
                          .filter(setting -> setting.getName().equals(name))
                          .findAny()
                          .orElseGet(() -> new SettingImpl(namespace, name, defaultValue));
   }
}
