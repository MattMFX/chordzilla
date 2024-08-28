module chordzilla {
    requires static lombok;
    requires spring.context;
    requires org.slf4j;
    requires io.grpc;
    requires io.grpc.services;
    exports br.edu.ufabc.mfmachado.chordzilla.api.entrypoint;
}