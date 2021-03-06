/*
 * Copyright (C) 2019 Becoming Machinic Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.becomingmachinic.kafka.collections;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Base HashStream that all HashStream implementations should extend.
 * 
 * @author Caleb Shingledecker
 *
 */
public abstract class HashStream extends OutputStream implements AutoCloseable {
	
	@Override
	public abstract void write(int b) throws IOException;
	
	@Override
	public abstract void write(byte[] b) throws IOException;
	
	@Override
	public abstract void write(byte[] b, int off, int len) throws IOException;
	
	public abstract Hash getHashes() throws IOException;
	
}