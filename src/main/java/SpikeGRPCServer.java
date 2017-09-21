import io.grpc.Server;
import io.grpc.ServerBuilder;
import spikegrpc.SpikeGRPCGrpc;

import java.io.IOException;

public class SpikeGRPCServer {

    private final int port;
    private final Server server;

    public SpikeGRPCServer(int port) {
        this.port = port;
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(this.port).addService(new SpikeGRPCService());
        this.server = serverBuilder.build();
    }

    public void start() throws IOException {
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (SpikeGRPCServer.this.server != null) {
                    SpikeGRPCServer.this.server.shutdown();
                }
            }
        });
    }

    public static void main(String[] args) throws IOException {
        SpikeGRPCServer spikeGRPCServer = new SpikeGRPCServer(8980);
        spikeGRPCServer.start();
    }

    private static class SpikeGRPCService extends SpikeGRPCGrpc.SpikeGRPCImplBase {


    }

}
