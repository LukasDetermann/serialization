package io.determann.serialization.view;

import java.util.List;
import java.util.stream.Collectors;

public final class PropertySerializationView<TYPE> implements SerializationView<TYPE, PropertySerializationView<TYPE>>
{
   private final PropertyViewImpl<TYPE> propertyViewImpl;

   public PropertySerializationView()
   {
      this.propertyViewImpl = new PropertyViewImpl<>();
   }

   private PropertySerializationView(PropertyViewImpl<TYPE> propertyViewImpl)
   {
      this.propertyViewImpl = propertyViewImpl;
   }

   public <PROPERTY> PropertySerializationView<TYPE> with(SerializationProperty<TYPE, PROPERTY, ?> property)
   {
      return new PropertySerializationView<>(propertyViewImpl.with(property));
   }

   public <PROPERTY> PropertySerializationView<TYPE> with(SerializationProperty<TYPE, PROPERTY, ?> property,
                                                          PropertySetting propertySetting)
   {
      return new PropertySerializationView<>(propertyViewImpl.with(property, propertySetting));
   }

   @Override
   public PropertySerializationView<TYPE> withSetting(TypeSetting setting)
   {
      return new PropertySerializationView<>(propertyViewImpl.with(setting));
   }

   public List<SerializationProperty<TYPE, Object, ?>> getProperties()
   {
      return propertyViewImpl.getProperties()
                             .stream()
                             .map(property -> ((SerializationProperty<TYPE, Object, ?>) property))
                             .collect(Collectors.toList());
   }

   @Override
   public List<TypeSetting> getSettings()
   {
      return propertyViewImpl.getSettings();
   }
}
