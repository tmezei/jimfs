/*
 * Copyright 2013 Google Inc.
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

package com.google.jimfs.attribute;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;

/**
 * Static registry of {@link AttributeProvider} implementations for the standard set of file
 * attribute views JimFS supports.
 *
 * <p>The supported views are:
 *
 * <table>
 *   <tr>
 *     <td><b>View Name</b></td>
 *     <td><b>View Interface</b></td>
 *     <td><b>Attributes Interface</b></td>
 *   </tr>
 *   <tr>
 *     <td>{@code "basic"}</td>
 *     <td>{@link java.nio.file.attribute.BasicFileAttributeView BasicFileAttributeView}</td>
 *     <td>{@link java.nio.file.attribute.BasicFileAttributes BasicFileAttributes}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code "owner"}</td>
 *     <td>{@link java.nio.file.attribute.FileOwnerAttributeView FileOwnerAttributeView}</td>
 *     <td>--</td>
 *   </tr>
 *   <tr>
 *     <td>{@code "posix"}</td>
 *     <td>{@link java.nio.file.attribute.PosixFileAttributeView PosixFileAttributeView}</td>
 *     <td>{@link java.nio.file.attribute.PosixFileAttributes PosixFileAttributes}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code "unix"}</td>
 *     <td>--</td>
 *     <td>--</td>
 *   </tr>
 *   <tr>
 *     <td>{@code "dos"}</td>
 *     <td>{@link java.nio.file.attribute.DosFileAttributeView DosFileAttributeView}</td>
 *     <td>{@link java.nio.file.attribute.DosFileAttributes DosFileAttributes}</td>
 *   </tr>
 *   <tr>
 *     <td>{@code "acl"}</td>
 *     <td>{@link java.nio.file.attribute.AclFileAttributeView AclFileAttributeView}</td>
 *     <td>--</td>
 *   </tr>
 *   <tr>
 *     <td>{@code "user"}</td>
 *     <td>{@link java.nio.file.attribute.UserDefinedFileAttributeView UserDefinedFileAttributeView}</td>
 *     <td>--</td>
 *   </tr>
 * </table>
 *
 * @author Colin Decker
 */
public final class StandardAttributeProviders {

  private StandardAttributeProviders() {}

  private static final ImmutableMap<String, AttributeProvider> PROVIDERS =
      new ImmutableMap.Builder<String, AttributeProvider>()
          .put("basic", new BasicAttributeProvider())
          .put("owner", new OwnerAttributeProvider())
          .put("posix", new PosixAttributeProvider())
          .put("dos", new DosAttributeProvider())
          .put("acl", new AclAttributeProvider())
          .put("user", new UserDefinedAttributeProvider())
          .build();

  /**
   * Returns the attribute provider for the given view, or {@code null} if the given view is not
   * one of the attribute views this supports.
   */
  @Nullable
  public static AttributeProvider get(String view) {
    AttributeProvider provider = PROVIDERS.get(view);

    if (provider == null && view.equals("unix")) {
      // create a new UnixAttributeProvider per file system, as it does some caching that should be
      // cleaned up when the file system is garbage collected
      return new UnixAttributeProvider();
    }

    return provider;
  }
}
