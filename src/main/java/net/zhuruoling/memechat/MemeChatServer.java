package net.zhuruoling.memechat;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;

public class MemeChatServer implements DedicatedServerModInitializer {
    private final Logger logger = LogUtils.getLogger();
    @Override
    public void onInitializeServer() {
        logger.info("Hello World!");
    }
}
