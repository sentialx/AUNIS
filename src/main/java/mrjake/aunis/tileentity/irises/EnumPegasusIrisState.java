package mrjake.aunis.tileentity.irises;


import mrjake.aunis.stargate.network.StargatePos;

public enum EnumPegasusIrisState {

    CLOSING,
    OPENING,

    CLOSED,
    OPENED,

    CLOSE,
    OPEN;


    public static EnumPegasusIrisState pegasusIrisState;

    public void setPegasusIrisState(EnumPegasusIrisState state, StargatePos pos) {
        switch (state) {
            case OPEN:
                pegasusIrisState = OPENING;

                // TODO fucking animation shit

                pegasusIrisState = OPENED;

            case CLOSE:
                pegasusIrisState = CLOSING;

                // TODO fucking animation shit

                pegasusIrisState = CLOSED;

        }
    }

}
