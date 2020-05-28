package com.egls.server.utils.cipher.rsa;

/**
 * @author mayer - [Created on 2019-08-03 14:15]
 */
interface Rsa {

    enum KeyBits {

        /**
         * 1024bit,128byte
         */
        BITS_1024(1024),

        /**
         * 2048bit,256byte
         */
        BITS_2048(2048);

        public final int keyBitsLength;

        public final int encryptBlockLength;

        public final int decryptBlockLength;

        KeyBits(int keyBitsLength) {
            this.keyBitsLength = keyBitsLength;
            //算出密钥的字节数
            int keyBytesLength = keyBitsLength / 8;
            //RSA算法规定,每次加密的数据不能超过密钥的长度减去11
            this.encryptBlockLength = keyBytesLength - 11;
            //RSA算法每次加密得到的密文长度是密钥的长度
            this.decryptBlockLength = keyBytesLength;
        }

    }

    KeyBits RSA_KEY_MODE = KeyBits.BITS_1024;

    String KEY_ALGORITHM_NAME = "RSA";

    String CIPHER_ALGORITHM_NAME = "RSA/ECB/PKCS1Padding";

    String SIGNATURE_ALGORITHM_NAME = "SHA1withRSA";

}
