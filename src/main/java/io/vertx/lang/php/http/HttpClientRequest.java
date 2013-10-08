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
package io.vertx.lang.php.http;

import io.vertx.lang.php.Gettable;
import io.vertx.lang.php.MultiMapArray;
import io.vertx.lang.php.Settable;
import io.vertx.lang.php.buffer.Buffer;
import io.vertx.lang.php.streams.ExceptionSupport;
import io.vertx.lang.php.streams.WriteStream;
import io.vertx.lang.php.util.HandlerFactory;
import io.vertx.lang.php.util.PhpTypes;

import com.caucho.quercus.annotation.Optional;
import com.caucho.quercus.env.BooleanValue;
import com.caucho.quercus.env.Env;
import com.caucho.quercus.env.StringValue;
import com.caucho.quercus.env.NumberValue;
import com.caucho.quercus.env.Value;

/**
 * A PHP compatible implementation of the Vert.x HttpClientRequest.
 * 
 * @author Jordan Halterman
 */
public class HttpClientRequest implements WriteStream<HttpClientRequest>, ExceptionSupport<HttpClientRequest>, Gettable, Settable {

  private org.vertx.java.core.http.HttpClientRequest request;

  public HttpClientRequest(org.vertx.java.core.http.HttpClientRequest request) {
    this.request = request;
  }

  public HttpClientRequest(Env env, org.vertx.java.core.http.HttpClientRequest request) {
    this.request = request;
  }

  @Override
  public Value __getField(Env env, StringValue name) {
    return env.wrapJava(this).callMethod(env, name);
  }

  @Override
  public void __setField(Env env, StringValue name, Value value) {
    env.wrapJava(this).callMethod(env, name, value);
  }

  /**
   * Returns request headers.
   */
  public Value headers(Env env) {
    return env.wrapJava(new MultiMapArray(request.headers()));
  }

  /**
   * Puts an HTTP header.
   */
  public Value putHeader(Env env, StringValue name, Value value) {
    request.putHeader(name.toString(), value.toString());
    return env.wrapJava(this);
  }

  /**
   * Forces the head of the request to be written before end.
   */
  public Value sendHead(Env env) {
    request.sendHead();
    return env.wrapJava(this);
  }

  @Override
  public HttpClientRequest write(Env env, Value data, @Optional StringValue enc) {
    if (enc != null && !enc.isDefault()) {
      if (data.isObject()) {
        request.write(((Buffer) data.toJavaObject(env, Buffer.class)).__toVertxBuffer());
      }
      else {
        request.write(data.toString(), enc.toString());
      }
    }
    else {
      if (data.isObject()) {
        request.write(((Buffer) data.toJavaObject(env, Buffer.class)).__toVertxBuffer());
      }
      else {
        request.write(data.toString());
      }
    }
    return this;
  }

  public HttpClientRequest continueHandler(Env env, Value handler) {
    PhpTypes.assertCallable(env, handler,
        "Handler argument to Vertx\\Http\\HttpClientRequest::continueHandler() must be callable.");
    request.continueHandler(HandlerFactory.createVoidHandler(env, handler));
    return this;
  }

  public void end() {
    request.end();
  }

  public void end(Env env) {
    end();
  }

  public void end(Env env, Value data) {
    request.end(data.toString());
  }

  @Override
  public HttpClientRequest drainHandler(Env env, Value handler) {
    PhpTypes.assertCallable(env, handler,
        "Handler argument to Vertx\\Http\\HttpClientRequest::drainHandler() must be callable.");
    request.drainHandler(HandlerFactory.createVoidHandler(env, handler));
    return this;
  }

  public HttpClientRequest timeout(Env env, Value timeoutMs) {
    request.setTimeout(timeoutMs.toInt());
    return this;
  }

  public HttpClientRequest chunked(Env env, BooleanValue chunked) {
    request.setChunked(chunked.toBoolean());
    return this;
  }

  public BooleanValue chunked(Env env) {
    return BooleanValue.create(request.isChunked());
  }

  @Override
  public HttpClientRequest writeQueueMaxSize(Env env, NumberValue size) {
    request.setWriteQueueMaxSize(size.toInt());
    return this;
  }

  @Override
  public BooleanValue writeQueueFull(Env env) {
    return BooleanValue.create(request.writeQueueFull());
  }

  @Override
  public HttpClientRequest exceptionHandler(Env env, Value handler) {
    PhpTypes.assertCallable(env, handler,
        "Handler argument to Vertx\\Http\\HttpClientRequest::exceptionhandler() must be callable.");
    request.exceptionHandler(HandlerFactory.createExceptionHandler(env, handler));
    return this;
  }

  public String toString() {
    return "php:Vertx\\Http\\HttpClientRequest";
  }

}
