package tools.packet;

import client.MapleCharacter;
import client.MapleClient;
import constants.ServerConstants;
import handling.MaplePacket;
import handling.SendPacketOpcode;
import handling.login.LoginServer;
import handling.login.handler.Balloon;
import tools.HexTool;
import tools.data.output.MaplePacketLittleEndianWriter;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoginPacket {

    public static MaplePacket getHello(final short mapleVersion, final byte[] sendIv, final byte[] recvIv) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(16);
        mplew.writeShort(13); // 13 = MSEA, 14 = GlobalMS, 15 = EMS
        mplew.writeShort(mapleVersion);
        mplew.write(new byte[]{0, 0});
        // mplew.writeMapleAsciiString(ServerConstants.MAPLE_PATCH);
        mplew.write(recvIv);
        mplew.write(sendIv);
        mplew.write(4); // 7 = MSEA, 8 = GlobalMS, 5 = Test Server

        return mplew.getPacket();
    }

    public static MaplePacket getPing() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(16);
        mplew.writeShort(SendPacketOpcode.PING.getValue());
        return mplew.getPacket();
    }

    public static MaplePacket genderNeeded(MapleClient c) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);
        mplew.writeShort(SendPacketOpcode.CHOOSE_GENDER.getValue());
        mplew.writeMapleAsciiString(c.getAccountName());

        return mplew.getPacket();
    }

    /**
     * * * * *
     * 3：身份证被删除或被阻挡 4：不正确的密码 5：不是一个注册的身份证 6：系统错误 7：已登录 8：系统错误 9：系统错误
     * 10：不能处理这么多连接 11：只有20岁以上的用户可以使用该频道 13：无法登录此知识产权 14：错误的网关或个人信息和奇怪的韩国按钮
     * 15：处理请求与韩国按钮！ 16：请通过电子邮件验证您的帐户… 17：错误的网关或个人信息 21：请通过电子邮件验证您的帐户…
     * 23：许可协议 25：欧洲枫叶欧洲公告 27：一些奇怪的完整的客户端通知，可能为试用版本 32：知识产权封锁 84：请重新通过网站-->
     * 0x07 recv响应00 / 01 /
     */
    public static MaplePacket getLoginFailed(final int reason) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(16);
        mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        mplew.writeInt(reason);
        mplew.writeShort(0);

        return mplew.getPacket();
    }

    public static MaplePacket getPermBan(final byte reason) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(16);
        mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        mplew.writeShort(2); // Account is banned
        mplew.write(0);
        mplew.write(reason);
        mplew.write(HexTool.getByteArrayFromHexString("01 01 01 01 00"));

        return mplew.getPacket();
    }

    public static MaplePacket getTempBan(final long timestampTill, final byte reason) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(17);
        mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        mplew.write(2);
        mplew.write(HexTool.getByteArrayFromHexString("00 00 00 00 00"));
        mplew.write(reason);
        mplew.writeLong(timestampTill); // Tempban date is handled as a 64-bit long, number of 100NS intervals since 1/1/1601. Lulz.

        return mplew.getPacket();
    }

    public static MaplePacket getGenderChanged(final MapleClient client) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.GENDER_SET.getValue());
        mplew.write(0);
        mplew.writeMapleAsciiString(client.getAccountName());
        mplew.writeMapleAsciiString(String.valueOf(client.getAccID()));

        return mplew.getPacket();
    }

    public static MaplePacket getGenderNeeded(final MapleClient client) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHOOSE_GENDER.getValue());
        mplew.writeMapleAsciiString(client.getAccountName());

        return mplew.getPacket();
    }

    public static MaplePacket getAuthSuccessRequest(final MapleClient client) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        mplew.write(0);
        mplew.writeInt(client.getAccID());
        mplew.write(client.getGender());
        mplew.writeShort(client.isGm() ? 1 : 0); // Admin byte
        //mplew.writeInt(0);
        mplew.writeMapleAsciiString(client.getAccountName());
        mplew.write(HexTool.getByteArrayFromHexString("00 00 00 03 01 00 00 00 E2 ED A3 7A FA C9 01"));
        mplew.writeInt(0);
        mplew.writeLong(0L);
        mplew.writeMapleAsciiString(String.valueOf(client.getAccID()));
        mplew.writeMapleAsciiString(client.getAccountName());
        mplew.write(1);

        // mplew.writeLong(0);
        //  mplew.writeLong(0);
        //  mplew.writeLong(0);
        return mplew.getPacket();
    }

    public static MaplePacket deleteCharResponse(final int cid, final int state) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DELETE_CHAR_RESPONSE.getValue());
        mplew.writeInt(cid);
        mplew.write(state);

        return mplew.getPacket();
    }

    public static MaplePacket secondPwError(final byte mode) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);
        mplew.writeShort(SendPacketOpcode.SECONDPW_ERROR.getValue());
        mplew.write(mode);

        return mplew.getPacket();
    }

    public static MaplePacket getServerList(final int serverId, final String serverName, final Map<Integer, Integer> channelLoad) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SERVERLIST.getValue());
        mplew.write(serverId); // 0 = Aquilla, 1 = bootes, 2 = cass, 3 = delphinus
        mplew.writeMapleAsciiString(serverName);
        mplew.write(LoginServer.getFlag());
        mplew.writeMapleAsciiString(LoginServer.getEventMessage());
        mplew.writeShort(100);
        mplew.writeShort(100);

        int lastChannel = 1;
        Set<Integer> channels = channelLoad.keySet();
        for (int i = 30; i > 0; i--) {
            if (channels.contains(i)) {
                lastChannel = i;
                break;
            }
        }
        mplew.write(lastChannel);
        mplew.writeInt(500);

        int load;
        for (int i = 1; i <= lastChannel; i++) {
            if (channels.contains(i)) {
                load = channelLoad.get(i);
            } else {
                load = 1200;
            }
            mplew.writeMapleAsciiString(serverName + "-" + i);
            mplew.writeInt(load);
            mplew.write(serverId);
            mplew.writeShort(i - 1);
        }
        mplew.writeShort(ServerConstants.getBalloons().size());
        for (Balloon balloon : ServerConstants.getBalloons()) {
            mplew.writeShort(balloon.nX);
            mplew.writeShort(balloon.nY);
            mplew.writeMapleAsciiString(balloon.sMessage);
        }
        return mplew.getPacket();
    }

    public static MaplePacket getEndOfServerList() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SERVERLIST.getValue());
        mplew.write(0xFF);

        return mplew.getPacket();
    }

    public static MaplePacket getServerStatus(final int status) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.SERVERSTATUS.getValue());
        mplew.writeShort(status); // 0 - Normal, 1 - Highly populated, 2 - Full
        return mplew.getPacket();
    }

    public static MaplePacket getCharList(final boolean secondpw, final List<MapleCharacter> chars, int charslots) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHARLIST.getValue());
        mplew.write(0);
        mplew.writeInt(0);
        mplew.write(chars.size());
        for (final MapleCharacter chr : chars) {
            addCharEntry(mplew, chr, false);
        }
        mplew.writeShort(3);
        mplew.writeInt(charslots);
        return mplew.getPacket();
    }

    public static MaplePacket addNewCharEntry(final MapleCharacter chr, final boolean worked) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ADD_NEW_CHAR_ENTRY.getValue());
        mplew.write(worked ? 0 : 1);
        addCharEntry(mplew, chr, false);
        return mplew.getPacket();
    }

    public static MaplePacket charNameResponse(final String charname, final boolean nameUsed) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CHAR_NAME_RESPONSE.getValue());
        mplew.writeMapleAsciiString(charname);
        mplew.write(nameUsed ? 1 : 0);
        return mplew.getPacket();
    }

    private static void addCharEntry(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr, boolean viewAll) {
        PacketHelper.addCharStats(mplew, chr);
        PacketHelper.addCharLook(mplew, chr, true, viewAll);
        mplew.write(0); //<-- who knows
    }
}
