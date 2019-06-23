package im.status.keycard;
import javacard.framework.Util;
import javacard.security.*;

public class Phonon {
    public byte networkId;
    public byte assetId;
    private ECPrivateKey owner;
    public short amount;
    public byte decimals;
    public byte[] extraData;

    Phonon(byte _networkId, byte _assetId, byte[] _owner, short _amount, byte _decimals, byte[] _extraData) {
        // if (_owner.length != 32 || _extraData.length != 33) {
            // return;
        // }
        // Copy data
        this.networkId = _networkId;
        this.assetId = _assetId;
        this.amount = _amount;
        this.decimals = _decimals;
        Util.arrayCopy(this.extraData, (short) 0, _extraData, (short) 0, (short) _extraData.length);

        // Save the private key
        // TODO: Probably want to do this in KeycardApplet and just have an ECPrivateKey passed in here
        Crypto crypto = new Crypto();
        SECP256k1 secp256k1 = new SECP256k1(crypto);
        this.owner = (ECPrivateKey) KeyBuilder.buildKey(KeyBuilder.TYPE_EC_FP_PRIVATE, SECP256k1.SECP256K1_KEY_SIZE, false);
        secp256k1.setCurveParameters(this.owner);
        this.owner.setS(_owner, (short) 0, Crypto.KEY_SECRET_SIZE);

    }

    public byte[] serialize() {
        // TODO: Should probably move SERIALIZED_PHONON_LEN to this file
        byte[] d = new byte[PhononNetwork.SERIALIZED_PHONON_LEN];
        short off = 0;
        d[off] = this.networkId; off++;
        d[off] = this.assetId; off++;
        byte[] a = new byte[2];
        Util.getShort(a, this.amount);
        d[off] = a[0]; off++;
        d[off] = a[1]; off++;
        d[off] = this.decimals; off++;
        return d;
    }

}