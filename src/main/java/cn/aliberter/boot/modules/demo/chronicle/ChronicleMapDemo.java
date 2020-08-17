package cn.aliberter.boot.modules.demo.chronicle;

import cn.aliberter.boot.common.utils.GlobalIdUtil;
import cn.aliberter.boot.modules.user.entity.SysUser;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.system.SystemUtil;
import net.openhft.chronicle.map.ChronicleMap;

/**
 * @author : aliberter
 * @version : 1.0
 */
public class ChronicleMapDemo {

    public static void main(String[] args) {

        long startMemory = SystemUtil.getRuntimeInfo().getUsableMemory();

        int size = 5000000;

        SysUser sample = new SysUser().setId(GlobalIdUtil.simpleIdStr())
                .setUsername("sample").setPassword("sample");

        try (ChronicleMap<Integer, SysUser> proxyMap = ChronicleMap
                .of(Integer.class, SysUser.class)
                .entries(size)
                .averageValue(sample)
                .averageValueSize(1024)
                .create()) {
            for (int i = 0; i < size; i++) {
                proxyMap.put(i, new SysUser().setId(GlobalIdUtil.simpleIdStr())
                        .setUsername("张三" + i).setPassword("" + i));
            }

            long endMemory = SystemUtil.getRuntimeInfo().getUsableMemory();

            Console.log("{}条数据存放完毕，占用堆外内存{}", size, FileUtil.readableFileSize((long) NumberUtil.sub(startMemory, endMemory)));

            TimeInterval timer = DateUtil.timer();
            Console.log(proxyMap.get(2500000));
            Console.log("查找一条数据耗时{}秒", timer.interval());

        }
    }

}
