package io.determann.serialization.view;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class PropertyView<TYPE> implements SerializationView<TYPE, PropertyView<TYPE>>,
                                                 DeSerializationView<TYPE, PropertyView<TYPE>>
{
   private final PropertyViewImpl<TYPE> propertyViewImpl;

   PropertyView(Supplier<TYPE> instanceSupplier)
   {
      this.propertyViewImpl = new PropertyViewImpl<>(instanceSupplier);
   }

   private PropertyView(PropertyViewImpl<TYPE> propertyViewImpl)
   {
      this.propertyViewImpl = propertyViewImpl;
   }

   public <PROPERTY> PropertyView<TYPE> with(ReadAndWriteProperty<TYPE, PROPERTY,?> property)
   {
      return new PropertyView<>(propertyViewImpl.with(property));
   }

   public <PROPERTY> PropertyView<TYPE> with(ReadAndWriteProperty<TYPE, PROPERTY,?> property,
                                             PropertySetting propertySetting)
   {
      return new PropertyView<>(propertyViewImpl.with(property, propertySetting));
   }

   public List<ReadAndWriteProperty<TYPE, Object,?>> getProperties()
   {
      return propertyViewImpl.getProperties()
                             .stream()
                             .map(property -> ((ReadAndWriteProperty<TYPE, Object,?>) property))
                             .collect(Collectors.toList());
   }

   @Override
   public PropertyView<TYPE> withSetting(TypeSetting setting)
   {
      return new PropertyView<>(propertyViewImpl.with(setting));
   }

   @Override
   public List<TypeSetting> getSettings()
   {
      return propertyViewImpl.getSettings();
   }

   public Supplier<TYPE> getInstanceSupplier()
   {
      return propertyViewImpl.getInstanceSupplier();
   }
}
