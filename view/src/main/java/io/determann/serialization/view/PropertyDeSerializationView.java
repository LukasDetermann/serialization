package io.determann.serialization.view;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class PropertyDeSerializationView<TYPE> implements DeSerializationView<TYPE, PropertyDeSerializationView<TYPE>>
{
   private final PropertyViewImpl<TYPE> propertyViewImpl;

   public PropertyDeSerializationView(Supplier<TYPE> instanceSupplier)
   {
      propertyViewImpl = new PropertyViewImpl<>(instanceSupplier);
   }

   private PropertyDeSerializationView(PropertyViewImpl<TYPE> propertyViewImpl)
   {
      this.propertyViewImpl = propertyViewImpl;
   }

   public <PROPERTY> PropertyDeSerializationView<TYPE> with(DeSerializationProperty<TYPE, PROPERTY,?> property)
   {
      return new PropertyDeSerializationView<>(propertyViewImpl.with(property));
   }

   public <PROPERTY> PropertyDeSerializationView<TYPE> with(DeSerializationProperty<TYPE, PROPERTY,?> property,
                                                            PropertySetting propertySetting)
   {
      return new PropertyDeSerializationView<>(propertyViewImpl.with(property, propertySetting));
   }

   @Override
   public PropertyDeSerializationView<TYPE> withSetting(TypeSetting setting)
   {
      return new PropertyDeSerializationView<>(propertyViewImpl.with(setting));
   }

   public List<DeSerializationProperty<TYPE, Object,?>> getProperties()
   {
      return propertyViewImpl.getProperties()
                             .stream()
                             .map(property -> ((DeSerializationProperty<TYPE, Object,?>) property))
                             .collect(Collectors.toList());
   }

   public Supplier<TYPE> getInstanceSupplier()
   {
      return propertyViewImpl.getInstanceSupplier();
   }

   @Override
   public List<TypeSetting> getSettings()
   {
      return propertyViewImpl.getSettings();
   }
}
