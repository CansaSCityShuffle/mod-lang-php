/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the MIT License (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertx.lang.php.parsetools;

import io.vertx.lang.php.buffer.Buffer;
import io.vertx.lang.php.util.HandlerFactory;
import io.vertx.lang.php.util.PhpTypes;

import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.NumberValue;
import com.caucho.quercus.env.Value;

/**
 * A PHP compatible implementation of the Vert.x RecordParser.
 * 
 * @author Jordan Halterman
 */
public class RecordParser {

  private org.vertx.java.core.parsetools.RecordParser parser;

  private RecordParser(org.vertx.java.core.parsetools.RecordParser parser) {
    this.parser = parser;
  }

  private RecordParser(Env env, org.vertx.java.core.parsetools.RecordParser parser) {
    this.parser = parser;
  }

  /**
   * Creates a new RecordParser in delimited mode.
   */
  public static RecordParser newDelimited(Env env, Value delim, Value handler) {
    PhpTypes.assertNotNull(env, delim);
    PhpTypes.assertCallable(env, handler,
        "Handler argument to Vertx\\ParseTools\\RecordParser::newDelimited() must be callable.");
    return new RecordParser(org.vertx.java.core.parsetools.RecordParser.newDelimited(delim.toString(),
        HandlerFactory.createBufferHandler(env, handler)));
  }

  /**
   * Creates a new RecordParser in fixed size mode.
   */
  public static RecordParser newFixed(Env env, NumberValue size, Value handler) {
    PhpTypes.assertNotNull(env, size);
    PhpTypes.assertCallable(env, handler,
        "Handler argument to Vertx\\ParseTools\\RecordParser::newFixed() must be callable.");
    return new RecordParser(org.vertx.java.core.parsetools.RecordParser.newFixed(size.toInt(),
        HandlerFactory.createBufferHandler(env, handler)));
  }

  /**
   * Sets the parser to fixed size mode.
   */
  public void fixedSizeMode(Env env, NumberValue size) {
    PhpTypes.assertNotNull(env, size);
    parser.fixedSizeMode(size.toInt());
  }

  /**
   * Sets the parser to delimited mode.
   */
  public void delimitedMode(Env env, Value delim) {
    PhpTypes.assertNotNull(env, delim);
    parser.delimitedMode(delim.toString());
  }

  /**
   * Feeds the parser with data.
   */
  public void handle(org.vertx.java.core.buffer.Buffer buffer) {
    parser.handle(buffer);
  }

  /**
   * Feeds the parser with data.
   */
  public void handle(Env env, Buffer buffer) {
    handle(buffer.__toVertxBuffer());
  }

  /**
   * Sets the parser output handler.
   */
  public void setOutput(Env env, Value handler) {
    PhpTypes.assertCallable(env, handler,
        "Handler argument to Vertx\\ParseTools\\RecordParser::setOutput() must be callable.");
    parser.setOutput(HandlerFactory.createBufferHandler(env, handler));
  }

}
