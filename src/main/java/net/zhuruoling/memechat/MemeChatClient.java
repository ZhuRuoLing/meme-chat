package net.zhuruoling.memechat;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;

public class MemeChatClient implements ClientModInitializer {
    private final Logger logger = LogUtils.getLogger();
    @Override
    public void onInitializeClient() {
        logger.info("Hello World!");
    }
}
