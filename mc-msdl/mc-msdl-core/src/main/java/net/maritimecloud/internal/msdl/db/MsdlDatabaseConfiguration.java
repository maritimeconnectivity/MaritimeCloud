/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.maritimecloud.internal.msdl.db;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import net.maritimecloud.msdl.MsdlProcessor;
import net.maritimecloud.msdl.model.MsdlFile;
import net.maritimecloud.msdl.model.Visitor;

/**
 *
 * @author Kasper Nielsen
 */
public class MsdlDatabaseConfiguration {

    static void addFiles(Path path, Consumer<? super Path> consumer) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    addFiles(entry, consumer);
                } else {
                    consumer.accept(entry);
                }
            }
        }
    }

    public static DefaultMsdlDatabase create(Path p) throws IOException {
        MsdlProcessor mp = new MsdlProcessor();

        DefaultMsdlDatabase db = new DefaultMsdlDatabase();
        // Path p = Paths.get("/Users/kasperni/dma-workspace/MaritimeCloudClient/src/main/msdl");
        mp.setSourceDirectory(p);

        addFiles(p, e -> mp.addFile(e));

        mp.addPlugin(new Visitor() {
            /** {@inheritDoc} */
            @Override
            public void visitFile(MsdlFile file) {
                db.addMsdlFile(file);
                super.visitFile(file);
            }
        });
        mp.executePlugins();
        return db;
    }
    //
    // public static void main(String[] args) throws Exception {
    // DefaultMsdlDatabase db = create();
    //
    // JsonMessageReader r = new JsonMessageReader("{\"f1\": 123, \"f2\": 22  }");
    // MessageDeclaration md = db.getMessage("testproject.B1");
    //
    // DynamicMessage dm = DynamicMessage.readFrom(md, r);
    //
    // PrintWriter w = new PrintWriter(System.out);
    // dm.writeTo(new JsonValueWriter(w, 0));
    // w.flush();
    //
    //
    // DefaultMmsClient mmsClient = (DefaultMmsClient) MmsClientConfiguration.create("mmsi:123123").build();
    //
    // BroadcastMessageDeclaration bmd = db.getBroadcastMessage("testproject.HelloWorld");
    //
    // DynamicBroadcastMessage m = DynamicBroadcastMessage.readFrom(bmd, new JsonMessageReader("{\"msg\": \"123\"}"));
    //
    // EndpointMethod em = db.getEndpointMethod("testproject.Hello.hid");
    // requireNonNull(em);
    //
    // System.out.println(new B1().setF1(123).toJSON());
    // DynamicMessage param = DynamicMessage.readFrom("", em.getParameters(), new JsonMessageReader(
    // "{\"b2\": {\"f1\": 1232}}"));
    //
    // for (;;) {
    // EndpointInvocationFuture<?> f = mmsClient.invokeRemote(MaritimeId.create("mmsi:123"), em.getFullName(),
    // param, param.serializer(), DynamicMessage.valueSerializer(em.getReturnType()));
    // System.out.println(((Message) f.get()).toJSON());
    // Thread.sleep(1000);
    // System.out.println("Sleep");
    // }
    // }
}
