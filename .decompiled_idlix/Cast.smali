package com.idlix;
public final class Cast {
    private final String character;
    private final String id;
    private final String name;
    private final String profilePath;

    public Cast()
    {
        this(0, 0, 0, 0, 15, 0);
        return;
    }

    public Cast(String p1, String p2, String p3, String p4)
    {
        this.id = p1;
        this.name = p2;
        this.character = p3;
        this.profilePath = p4;
        return;
    }

    public synthetic Cast(String p2, String p3, String p4, String p5, int p6, kotlin.jvm.internal.DefaultConstructorMarker p7)
    {
        if ((p6 & 1) != 0) {
            p2 = 0;
        }
        if ((p6 & 2) != 0) {
            p3 = 0;
        }
        if ((p6 & 4) != 0) {
            p4 = 0;
        }
        if ((p6 & 8) != 0) {
            p5 = 0;
        }
        this(p2, p3, p4, p5);
        return;
    }

    public static synthetic com.idlix.Cast copy$default(com.idlix.Cast p0, String p1, String p2, String p3, String p4, int p5, Object p6)
    {
        if ((p5 & 1) != 0) {
            p1 = p0.id;
        }
        if ((p5 & 2) != 0) {
            p2 = p0.name;
        }
        if ((p5 & 4) != 0) {
            p3 = p0.character;
        }
        if ((p5 & 8) != 0) {
            p4 = p0.profilePath;
        }
        return p0.copy(p1, p2, p3, p4);
    }

    public final String component1()
    {
        return this.id;
    }

    public final String component2()
    {
        return this.name;
    }

    public final String component3()
    {
        return this.character;
    }

    public final String component4()
    {
        return this.profilePath;
    }

    public final com.idlix.Cast copy(String p2, String p3, String p4, String p5)
    {
        return new com.idlix.Cast(p2, p3, p4, p5);
    }

    public boolean equals(Object p6)
    {
        if (this != p6) {
            if ((p6 instanceof com.idlix.Cast)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.id, ((com.idlix.Cast) p6).id)) {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.name, ((com.idlix.Cast) p6).name)) {
                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.character, ((com.idlix.Cast) p6).character)) {
                            if (kotlin.jvm.internal.Intrinsics.areEqual(this.profilePath, ((com.idlix.Cast) p6).profilePath)) {
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
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            return 1;
        }
    }

    public final String getCharacter()
    {
        return this.character;
    }

    public final String getId()
    {
        return this.id;
    }

    public final String getName()
    {
        return this.name;
    }

    public final String getProfilePath()
    {
        return this.profilePath;
    }

    public int hashCode()
    {
        int v0_4;
        int v1_0 = 0;
        if (this.id != null) {
            v0_4 = this.id.hashCode();
        } else {
            v0_4 = 0;
        }
        String v3_1;
        int v2_3 = (v0_4 * 31);
        if (this.name != null) {
            v3_1 = this.name.hashCode();
        } else {
            v3_1 = 0;
        }
        String v3_4;
        int v0_1 = ((v2_3 + v3_1) * 31);
        if (this.character != null) {
            v3_4 = this.character.hashCode();
        } else {
            v3_4 = 0;
        }
        int v2_1 = ((v0_1 + v3_4) * 31);
        if (this.profilePath != null) {
            v1_0 = this.profilePath.hashCode();
        }
        return (v2_1 + v1_0);
    }

    public String toString()
    {
        return new StringBuilder().append("Cast(id=").append(this.id).append(", name=").append(this.name).append(", character=").append(this.character).append(", profilePath=").append(this.profilePath).append(41).toString();
    }
}
