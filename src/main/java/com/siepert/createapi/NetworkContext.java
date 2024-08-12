package com.siepert.createapi;

import com.siepert.createlegacy.CreateLegacyConfigHolder;

import java.util.ArrayList;
import java.util.List;

public class NetworkContext {

    boolean infiniteSU = CreateLegacyConfigHolder.otherConfig.disableSU;

    private final List<KineticBlockInstance> blocksToActivate = new ArrayList<>();
    int networkSpeed = 0;
    int totalSU = 0;
    int scheduledConsumedSU = 0;

}
