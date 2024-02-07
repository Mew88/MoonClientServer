package server;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.jetbrains.annotations.Nullable;

import co.gongzh.procbridge.IDelegate;
import co.gongzh.procbridge.Server;
import server.user.User;

public class SocketServer {
	public static ArrayList<User> users = new ArrayList<>();
    private static int count = -1;

    public static void main(String[] args) throws FileNotFoundException {
        Server server = new Server(1337, new IDelegate() {
            @Override
            public @Nullable Object handleRequest(@Nullable String method, @Nullable Object payload) {
                switch (method) {
                    case "echo":
                        System.out.println(method + " " + payload.toString());
                        return payload;
                    case "start_tutorial":
                        String[] userProperties = payload.toString().split(":");
                        users.add(new User(userProperties[0], Boolean.parseBoolean(userProperties[1])));
                        count++;
                        System.out.println(users.size());
                        System.out.println(users.get(count).getProperties());
                        return payload;
                    case "isUser":
                        for(User user : users) {
                            if(payload.toString().equals(user.getMcName())) {
                                return "true";
                            }
                        }
                        return "false";
                }
                return payload;
            }
        });

        System.out.println("Started server on port " + server.getPort());
        server.start();
    }
}
