package mrjake.aunis.tileentity.irises;


import mrjake.aunis.stargate.network.StargatePos;

import static mrjake.aunis.tileentity.irises.EnumPegasusIrisState.pegasusIrisState;

public class IrisPegasusManager {

    public boolean pegasusIrisChange(String state, StargatePos pos){
        switch(state) {
            case "close":
                if (pegasusIrisState == EnumPegasusIrisState.OPENED) {
                    pegasusIrisState.setPegasusIrisState(EnumPegasusIrisState.CLOSE, pos);
                    return true;
                } else {
                    return false;
                }
            case "open":
                if (pegasusIrisState == EnumPegasusIrisState.CLOSED) {
                    pegasusIrisState.setPegasusIrisState(EnumPegasusIrisState.OPEN, pos);
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }
    }

}
