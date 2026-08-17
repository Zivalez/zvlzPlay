package com.idlix;
public final class Jeniusplay$Tracks {
    private final String file;
    private final String kind;
    private final String label;

    public Jeniusplay$Tracks(String p1, String p2, String p3)
    {
        this.kind = p1;
        this.file = p2;
        this.label = p3;
        return;
    }

    public static synthetic com.idlix.Jeniusplay$Tracks copy$default(com.idlix.Jeniusplay$Tracks p0, String p1, String p2, String p3, int p4, Object p5)
    {
        if ((p4 & 1) != 0) {
            p1 = p0.kind;
        }
        if ((p4 & 2) != 0) {
            p2 = p0.file;
        }
        if ((p4 & 4) != 0) {
            p3 = p0.label;
        }
        return p0.copy(p1, p2, p3);
    }

    public final String component1()
    {
        return this.kind;
    }

    public final String component2()
    {
        return this.file;
    }

    public final String component3()
    {
        return this.label;
    }

    public final com.idlix.Jeniusplay$Tracks copy(String p2, String p3, String p4)
    {
        return new com.idlix.Jeniusplay$Tracks(p2, p3, p4);
    }

    public boolean equals(Object p6)
    {
        if (this != p6) {
            if ((p6 instanceof com.idlix.Jeniusplay$Tracks)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.kind, ((com.idlix.Jeniusplay$Tracks) p6).kind)) {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.file, ((com.idlix.Jeniusplay$Tracks) p6).file)) {
                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.label, ((com.idlix.Jeniusplay$Tracks) p6).label)) {
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
            return 1;
        }
    }

    public final String getFile()
    {
        return this.file;
    }

    public final String getKind()
    {
        return this.kind;
    }

    public final String getLabel()
    {
        return this.label;
    }

    public int hashCode()
    {
        int v0_4;
        int v1_0 = 0;
        if (this.kind != null) {
            v0_4 = this.kind.hashCode();
        } else {
            v0_4 = 0;
        }
        int v0_1 = (((v0_4 * 31) + this.file.hashCode()) * 31);
        if (this.label != null) {
            v1_0 = this.label.hashCode();
        }
        return (v0_1 + v1_0);
    }

    public String toString()
    {
        return new StringBuilder().append("Tracks(kind=").append(this.kind).append(", file=").append(this.file).append(", label=").append(this.label).append(41).toString();
    }
}
