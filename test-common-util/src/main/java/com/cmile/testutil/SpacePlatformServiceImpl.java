package com.cmile.testutil;

import com.cmile.serviceutil.validators.space.SpacePlatformService;

public class SpacePlatformServiceImpl implements SpacePlatformService {

  @Override
  public Object fetchSpaceDetails() {
    return "test-space";
  }
}
