package com.becomingmachinic.kafka.collections.config;

import com.becomingmachinic.kafka.collections.CollectionConfig;
import com.becomingmachinic.kafka.collections.KafkaCollectionConfigurationException;

import java.util.Map;

public class NameConfigKey extends ConfigKey<String> {
	
	public NameConfigKey() {
		super(CollectionConfig.COLLECTION_NAME, null);
	}
	
	public String getValue(Map<? extends Object, Object> providedProperties) throws KafkaCollectionConfigurationException {
		String value = getAsString(providedProperties.get(this.name));
		if (value != null) {
			if (!value.replaceAll("[^0-9A-Za-z_-]+", "").equals(value)) {
				throw new KafkaCollectionConfigurationException("Parameter %s contains invalid characters. It should contain alphanumeric, hyphen and underscore only", this.name);
			}
		}
		if (value == null) {
			throw new KafkaCollectionConfigurationException("Property %s or %s is required", CollectionConfig.COLLECTION_NAME, CollectionConfig.COLLECTION_TOPIC);
		}
		return value;
	}
}
