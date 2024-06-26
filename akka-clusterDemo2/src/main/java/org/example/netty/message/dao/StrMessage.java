// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: str.proto
// Protobuf Java Version: 4.26.1

package org.example.netty.message.dao;

public final class StrMessage {
  private StrMessage() {}
  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 26,
      /* patch= */ 1,
      /* suffix= */ "",
      StrMessage.class.getName());
  }
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface msgOrBuilder extends
      // @@protoc_insertion_point(interface_extends:org.example.netty.message.msg)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * 自身属性的定义（后面的数字，代表属性的顺序）
     * </pre>
     *
     * <code>string msg = 1;</code>
     * @return The msg.
     */
    String getMsg();
    /**
     * <pre>
     * 自身属性的定义（后面的数字，代表属性的顺序）
     * </pre>
     *
     * <code>string msg = 1;</code>
     * @return The bytes for msg.
     */
    com.google.protobuf.ByteString
        getMsgBytes();
  }
  /**
   * <pre>
   * proto对象的定义（类似于Java的class定义）
   * 这里的msg名字随便填「Object1」，生成的时候类目为以上面定义的java_outer_classname
   * </pre>
   *
   * Protobuf type {@code org.example.netty.message.msg}
   */
  public static final class msg extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:org.example.netty.message.msg)
      msgOrBuilder {
  private static final long serialVersionUID = 0L;
    static {
      com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
        com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
        /* major= */ 4,
        /* minor= */ 26,
        /* patch= */ 1,
        /* suffix= */ "",
        msg.class.getName());
    }
    // Use msg.newBuilder() to construct.
    private msg(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
    }
    private msg() {
      msg_ = "";
    }

    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return internal_static_org_example_netty_message_msg_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return internal_static_org_example_netty_message_msg_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.example.netty.message.dao.StrMessage.msg.class, org.example.netty.message.dao.StrMessage.msg.Builder.class);
    }

    public static final int MSG_FIELD_NUMBER = 1;
    @SuppressWarnings("serial")
    private volatile Object msg_ = "";
    /**
     * <pre>
     * 自身属性的定义（后面的数字，代表属性的顺序）
     * </pre>
     *
     * <code>string msg = 1;</code>
     * @return The msg.
     */
    @Override
    public String getMsg() {
      Object ref = msg_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        msg_ = s;
        return s;
      }
    }
    /**
     * <pre>
     * 自身属性的定义（后面的数字，代表属性的顺序）
     * </pre>
     *
     * <code>string msg = 1;</code>
     * @return The bytes for msg.
     */
    @Override
    public com.google.protobuf.ByteString
        getMsgBytes() {
      Object ref = msg_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        msg_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    @Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!com.google.protobuf.GeneratedMessage.isStringEmpty(msg_)) {
        com.google.protobuf.GeneratedMessage.writeString(output, 1, msg_);
      }
      getUnknownFields().writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!com.google.protobuf.GeneratedMessage.isStringEmpty(msg_)) {
        size += com.google.protobuf.GeneratedMessage.computeStringSize(1, msg_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof org.example.netty.message.dao.StrMessage.msg)) {
        return super.equals(obj);
      }
      org.example.netty.message.dao.StrMessage.msg other = (org.example.netty.message.dao.StrMessage.msg) obj;

      if (!getMsg()
          .equals(other.getMsg())) return false;
      if (!getUnknownFields().equals(other.getUnknownFields())) return false;
      return true;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + MSG_FIELD_NUMBER;
      hash = (53 * hash) + getMsg().hashCode();
      hash = (29 * hash) + getUnknownFields().hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static org.example.netty.message.dao.StrMessage.msg parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.example.netty.message.dao.StrMessage.msg parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.example.netty.message.dao.StrMessage.msg parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.example.netty.message.dao.StrMessage.msg parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.example.netty.message.dao.StrMessage.msg parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.example.netty.message.dao.StrMessage.msg parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.example.netty.message.dao.StrMessage.msg parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseWithIOException(PARSER, input);
    }
    public static org.example.netty.message.dao.StrMessage.msg parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static org.example.netty.message.dao.StrMessage.msg parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseDelimitedWithIOException(PARSER, input);
    }

    public static org.example.netty.message.dao.StrMessage.msg parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static org.example.netty.message.dao.StrMessage.msg parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseWithIOException(PARSER, input);
    }
    public static org.example.netty.message.dao.StrMessage.msg parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessage
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(org.example.netty.message.dao.StrMessage.msg prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * <pre>
     * proto对象的定义（类似于Java的class定义）
     * 这里的msg名字随便填「Object1」，生成的时候类目为以上面定义的java_outer_classname
     * </pre>
     *
     * Protobuf type {@code org.example.netty.message.msg}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:org.example.netty.message.msg)
        org.example.netty.message.dao.StrMessage.msgOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return internal_static_org_example_netty_message_msg_descriptor;
      }

      @Override
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return internal_static_org_example_netty_message_msg_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.example.netty.message.dao.StrMessage.msg.class, org.example.netty.message.dao.StrMessage.msg.Builder.class);
      }

      // Construct using org.example.netty.message.dao.StrMessage.msg.newBuilder()
      private Builder() {

      }

      private Builder(
          BuilderParent parent) {
        super(parent);

      }
      @Override
      public Builder clear() {
        super.clear();
        bitField0_ = 0;
        msg_ = "";
        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return internal_static_org_example_netty_message_msg_descriptor;
      }

      @Override
      public org.example.netty.message.dao.StrMessage.msg getDefaultInstanceForType() {
        return getDefaultInstance();
      }

      @Override
      public org.example.netty.message.dao.StrMessage.msg build() {
        org.example.netty.message.dao.StrMessage.msg result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public org.example.netty.message.dao.StrMessage.msg buildPartial() {
        org.example.netty.message.dao.StrMessage.msg result = new org.example.netty.message.dao.StrMessage.msg(this);
        if (bitField0_ != 0) { buildPartial0(result); }
        onBuilt();
        return result;
      }

      private void buildPartial0(org.example.netty.message.dao.StrMessage.msg result) {
        int from_bitField0_ = bitField0_;
        if (((from_bitField0_ & 0x00000001) != 0)) {
          result.msg_ = msg_;
        }
      }

      @Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof org.example.netty.message.dao.StrMessage.msg) {
          return mergeFrom((org.example.netty.message.dao.StrMessage.msg)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(org.example.netty.message.dao.StrMessage.msg other) {
        if (other == getDefaultInstance()) return this;
        if (!other.getMsg().isEmpty()) {
          msg_ = other.msg_;
          bitField0_ |= 0x00000001;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
      }

      @Override
      public final boolean isInitialized() {
        return true;
      }

      @Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        if (extensionRegistry == null) {
          throw new NullPointerException();
        }
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              case 10: {
                msg_ = input.readStringRequireUtf8();
                bitField0_ |= 0x00000001;
                break;
              } // case 10
              default: {
                if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                  done = true; // was an endgroup tag
                }
                break;
              } // default:
            } // switch (tag)
          } // while (!done)
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.unwrapIOException();
        } finally {
          onChanged();
        } // finally
        return this;
      }
      private int bitField0_;

      private Object msg_ = "";
      /**
       * <pre>
       * 自身属性的定义（后面的数字，代表属性的顺序）
       * </pre>
       *
       * <code>string msg = 1;</code>
       * @return The msg.
       */
      public String getMsg() {
        Object ref = msg_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          msg_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <pre>
       * 自身属性的定义（后面的数字，代表属性的顺序）
       * </pre>
       *
       * <code>string msg = 1;</code>
       * @return The bytes for msg.
       */
      public com.google.protobuf.ByteString
          getMsgBytes() {
        Object ref = msg_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          msg_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * 自身属性的定义（后面的数字，代表属性的顺序）
       * </pre>
       *
       * <code>string msg = 1;</code>
       * @param value The msg to set.
       * @return This builder for chaining.
       */
      public Builder setMsg(
          String value) {
        if (value == null) { throw new NullPointerException(); }
        msg_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 自身属性的定义（后面的数字，代表属性的顺序）
       * </pre>
       *
       * <code>string msg = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearMsg() {
        msg_ = getDefaultInstance().getMsg();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 自身属性的定义（后面的数字，代表属性的顺序）
       * </pre>
       *
       * <code>string msg = 1;</code>
       * @param value The bytes for msg to set.
       * @return This builder for chaining.
       */
      public Builder setMsgBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) { throw new NullPointerException(); }
        checkByteStringIsUtf8(value);
        msg_ = value;
        bitField0_ |= 0x00000001;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:org.example.netty.message.msg)
    }

    // @@protoc_insertion_point(class_scope:org.example.netty.message.msg)
    private static final org.example.netty.message.dao.StrMessage.msg DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new org.example.netty.message.dao.StrMessage.msg();
    }

    public static org.example.netty.message.dao.StrMessage.msg getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<msg>
        PARSER = new com.google.protobuf.AbstractParser<msg>() {
      @Override
      public msg parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        Builder builder = newBuilder();
        try {
          builder.mergeFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.setUnfinishedMessage(builder.buildPartial());
        } catch (com.google.protobuf.UninitializedMessageException e) {
          throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
        } catch (java.io.IOException e) {
          throw new com.google.protobuf.InvalidProtocolBufferException(e)
              .setUnfinishedMessage(builder.buildPartial());
        }
        return builder.buildPartial();
      }
    };

    public static com.google.protobuf.Parser<msg> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<msg> getParserForType() {
      return PARSER;
    }

    @Override
    public org.example.netty.message.dao.StrMessage.msg getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_org_example_netty_message_msg_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_org_example_netty_message_msg_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\tstr.proto\022\031org.example.netty.message\"\022" +
      "\n\003msg\022\013\n\003msg\030\001 \001(\tB0\n\035org.example.netty." +
      "message.daoB\nStrMessageP\000\240\001\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_org_example_netty_message_msg_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_org_example_netty_message_msg_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_org_example_netty_message_msg_descriptor,
        new String[] { "Msg", });
    descriptor.resolveAllFeaturesImmutable();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
