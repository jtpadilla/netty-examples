package nia.chapter4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Asynchronous networking without Netty
 */
public class PlainNioServer {

    public void serve(int port) throws IOException {

        // Se instancia un ServerSocketChannel - NETTY
        ServerSocketChannel serverChannel = ServerSocketChannel.open();

        // Se configura no bloqueante
        serverChannel.configureBlocking(false);

        // Se obtiene el Socket desde el ServerSocketChannel- JAVA
        ServerSocket ss = serverChannel.socket();

        // Se indica el puerto por donde se recibiran las peticiones - JAVA
        InetSocketAddress address = new InetSocketAddress(port);

        // Se configura el puerto por el ServerSocket - JAVA
        ss.bind(address);

        // Se instancia un selector para recibir los eventos NIO
        Selector selector = Selector.open();

        // Se registra el selector el el canal servidor de NETTY para los eventos ACCEPT
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        // Se construye un buffer - JAVA
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());

        // Bucle infinito para recibir los eventos del selector
        for (;;){

            // El thread actual (el principal) se espera a que existe un evento
            try {
                selector.select();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Si se ha producido una excepcion en el selector, se sale del bucle y termina el programa
                break;
            }

            // Se obtiene el conjunto de eventos que han entrado por el selector
            Set<SelectionKey> readyKeys = selector.selectedKeys();

            // Se procesa cada uno de los eventos que hemos recibido
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {

                // Se obtiene el siguiente evento y se elimna de la lista
                SelectionKey key = iterator.next();
                iterator.remove();

                try {

                    // El evento indica la solicitud de una nueva conexion ?
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());
                        System.out.println("Accepted connection from " + client);
                    }

                    // El evento indica que ya se puede escribir por el socket ?
                    if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        while (buffer.hasRemaining()) {
                            if (client.write(buffer) == 0) {
                                break;
                            }
                        }
                        client.close();
                    }
                } catch (IOException ex) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {
                        // ignore on close
                    }
                }

            }
        }
    }
}

