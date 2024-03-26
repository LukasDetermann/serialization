package io.determann.serialization.view;

import java.util.List;
import java.util.Optional;

public sealed interface View<TYPE, SELF extends View<TYPE, SELF>> permits DeSerializationView,
                                                                          SerializationView
{
   SELF withSetting(TypeSetting setting);

   List<TypeSetting> getSettings();


   default Optional<TypeSetting> getSetting(String namespace, String name)
   {
      return getSettings().stream()
                          .filter(setting -> setting.getName().equals(name))
                          .findAny();
   }

   default TypeSetting getSetting(String namespace, String name, Object defaultValue)
   {
      return getSettings().stream()
                          .filter(setting -> setting.getName().equals(name))
                          .findAny()
                          .orElseGet(() -> new SettingImpl(namespace, name, defaultValue));
   }
}