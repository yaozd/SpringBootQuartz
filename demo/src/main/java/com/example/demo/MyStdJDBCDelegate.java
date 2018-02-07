package com.example.demo;

import org.quartz.TriggerKey;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.quartz.TriggerKey.triggerKey;

public class MyStdJDBCDelegate extends org.quartz.impl.jdbcjobstore.StdJDBCDelegate {
    /**
     * <p>
     * Get the names of all of the triggers in the given states that have
     * misfired - according to the given timestamp.  No more than count will
     * be returned.
     * </p>
     *
     * @param conn       the DB Connection
     * @param count      the most misfired triggers to return, negative for all
     * @param resultList Output parameter.  A List of
     *                   <code>{@link org.quartz.utils.Key}</code> objects.  Must not be null.
     * @return Whether there are more misfired triggers left to find beyond
     * the given count.
     */
    @Override
    public boolean hasMisfiredTriggersInState(Connection conn, String state1,
                                              long ts, int count, List<TriggerKey> resultList) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(rtp(SELECT_HAS_MISFIRED_TRIGGERS_IN_STATE));
            ps.setBigDecimal(1, new BigDecimal(String.valueOf(ts)));
            ps.setString(2, state1);
            rs = ps.executeQuery();

            boolean hasReachedLimit = false;
            while (rs.next() && (hasReachedLimit == false)) {
                if (resultList.size() == count) {
                    hasReachedLimit = true;
                } else {
                    String triggerName = rs.getString(COL_TRIGGER_NAME);
                    String groupName = rs.getString(COL_TRIGGER_GROUP);
                    //通过groupName过滤不属于当前程序的分组--任务调度
                    if ("123".equalsIgnoreCase(groupName) == false) {
                        continue;
                    }
                    resultList.add(triggerKey(triggerName, groupName));
                }
            }

            return hasReachedLimit;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
        }
    }

}
