/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zeppelin.interpreter;

import java.util.Properties;

import org.apache.zeppelin.interpreter.remote.mock.MockInterpreterA;
import org.apache.zeppelin.user.AuthenticationInfo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InterpreterTest {

  @Test
  public void testDefaultProperty() {
    Properties p = new Properties();
    p.put("p1", "v1");
    MockInterpreterA intp = new MockInterpreterA(p);

    assertEquals(1, intp.getProperty().size());
    assertEquals("v1", intp.getProperty().get("p1"));
    assertEquals("v1", intp.getProperty("p1"));
  }

  @Test
  public void testOverriddenProperty() {
    Properties p = new Properties();
    p.put("p1", "v1");
    MockInterpreterA intp = new MockInterpreterA(p);
    Properties overriddenProperty = new Properties();
    overriddenProperty.put("p1", "v2");
    intp.setProperty(overriddenProperty);

    assertEquals(1, intp.getProperty().size());
    assertEquals("v2", intp.getProperty().get("p1"));
    assertEquals("v2", intp.getProperty("p1"));
  }

  @Test
  public void testPropertyWithReplacedContextFields() {
    String noteId = "testNoteId";
    String paragraphTitle = "testParagraphTitle";
    String paragraphText = "testParagraphText";
    String paragraphId = "testParagraphId";
    String user = "username";
    InterpreterContext.set(new InterpreterContext(noteId,
        paragraphId,
        null,
        paragraphTitle,
        paragraphText,
        new AuthenticationInfo("testUser", "testTicket"),
        null,
        null,
        null,
        null,
        null,
        null));
    Properties p = new Properties();
    p.put("p1", "replName #{noteId}, #{paragraphTitle}, #{paragraphId}, #{paragraphText}, #{replName}, #{noteId}, #{user}," +
        " #{authenticationInfo}");
    MockInterpreterA intp = new MockInterpreterA(p);
    intp.setUserName(user);
    String actual = intp.getProperty("p1");
    InterpreterContext.remove();

    assertEquals(
        String.format("replName %s, #{paragraphTitle}, #{paragraphId}, #{paragraphText}, , %s, %s, #{authenticationInfo}", noteId,
            noteId, user),
        actual
    );
  }

}
