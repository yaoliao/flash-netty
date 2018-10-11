package the.flash.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import the.flash.util.SessionUtil;

@ChannelHandler.Sharable
public class AuthHandler extends ChannelInboundHandlerAdapter {
    public static final AuthHandler INSTANCE = new AuthHandler();

    private AuthHandler() {

    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!SessionUtil.hasLogin(ctx.channel())) {
            ctx.channel().close();
            // 这里是不用加这句的，因为MessageToMessageCodec编码器解码后就已经回收了ByteBuf了
            // 这里的msg已经不是ReferenceCounted的子类了,就算写了这没用了
            // 闪电侠大大厉害啊
            //ReferenceCountUtil.release(msg);
        } else {
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }
}
