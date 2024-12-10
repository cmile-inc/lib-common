package com.cmile.serviceutil.validators.space;

import java.util.Map;
import lombok.Data;

@Data
public class SpaceDetails {
  private String spaceId;
  private Map<String, ?> details;
}
