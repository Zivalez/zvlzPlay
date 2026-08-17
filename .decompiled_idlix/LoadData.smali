package com.idlix;
public final class LoadData {
    private final String id;
    private final String type;

    public LoadData(String p1, String p2)
    {
        this.id = p1;
        this.type = p2;
        return;
    }

    public static synthetic com.idlix.LoadData copy$default(com.idlix.LoadData p0, String p1, String p2, int p3, Object p4)
    {
        if ((p3 & 1) != 0) {
            p1 = p0.id;
        }
        if ((p3 & 2) != 0) {
            p2 = p0.type;
        }
        return p0.copy(p1, p2);
    }

    public final String component1()
    {
        return this.id;
    }

    public final String component2()
    {
        return this.type;
    }

    public final com.idlix.LoadData copy(String p2, String p3)
    {
        return new com.idlix.LoadData(p2, p3);
    }

    public boolean equals(Object p6)
    {
        if (this != p6) {
            if ((p6 instanceof com.idlix.LoadData)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.id, ((com.idlix.LoadData) p6).id)) {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.type, ((com.idlix.LoadData) p6).type)) {
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            return 1;
        }
    }

    public final String getId()
    {
        return this.id;
    }

    public final String getType()
    {
        return this.type;
    }

    public int hashCode()
    {
        return ((this.id.hashCode() * 31) + this.type.hashCode());
    }

    public String toString()
    {
        return new StringBuilder().append("LoadData(id=").append(this.id).append(", type=").append(this.type).append(41).toString();
    }
}
