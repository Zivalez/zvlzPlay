package com.idlix;
public final class Subtitle {
    private final String label;
    private final String lang;
    private final String path;

    public Subtitle(String p1, String p2, String p3)
    {
        this.lang = p1;
        this.label = p2;
        this.path = p3;
        return;
    }

    public static synthetic com.idlix.Subtitle copy$default(com.idlix.Subtitle p0, String p1, String p2, String p3, int p4, Object p5)
    {
        if ((p4 & 1) != 0) {
            p1 = p0.lang;
        }
        if ((p4 & 2) != 0) {
            p2 = p0.label;
        }
        if ((p4 & 4) != 0) {
            p3 = p0.path;
        }
        return p0.copy(p1, p2, p3);
    }

    public final String component1()
    {
        return this.lang;
    }

    public final String component2()
    {
        return this.label;
    }

    public final String component3()
    {
        return this.path;
    }

    public final com.idlix.Subtitle copy(String p2, String p3, String p4)
    {
        return new com.idlix.Subtitle(p2, p3, p4);
    }

    public boolean equals(Object p6)
    {
        if (this != p6) {
            if ((p6 instanceof com.idlix.Subtitle)) {
                if (kotlin.jvm.internal.Intrinsics.areEqual(this.lang, ((com.idlix.Subtitle) p6).lang)) {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.label, ((com.idlix.Subtitle) p6).label)) {
                        if (kotlin.jvm.internal.Intrinsics.areEqual(this.path, ((com.idlix.Subtitle) p6).path)) {
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

    public final String getLabel()
    {
        return this.label;
    }

    public final String getLang()
    {
        return this.lang;
    }

    public final String getPath()
    {
        return this.path;
    }

    public int hashCode()
    {
        return ((((this.lang.hashCode() * 31) + this.label.hashCode()) * 31) + this.path.hashCode());
    }

    public String toString()
    {
        return new StringBuilder().append("Subtitle(lang=").append(this.lang).append(", label=").append(this.label).append(", path=").append(this.path).append(41).toString();
    }
}
