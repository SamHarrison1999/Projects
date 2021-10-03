class Dequeue<T>
{ T data;
    Dequeue<T> next;

    public Dequeue(T x, Dequeue<T> c)
    { data = x;
        next = c;
    }
}

class ListException extends RuntimeException
{ public ListException(String s)
{ super(s);
}
}

public class LList<T>
{ private Dequeue<T> left;

    public LList()
    { left = null;
    }

    public void addLeft(T x)
    { left = new Dequeue<T>(x, left);
    }

    public void addRight(T x)
    { if (left==null)
        left = new Dequeue<T>(x, left);
    else
    { Dequeue<T> c = left;
        while (c.next != null)
            c = c.next;
        c.next = new Dequeue<T>(x, null);
    }
    }

    public T Left(int n)
    { Dequeue<T> c = left;
        for (int i = 0; i<n; i++)
        { if (c == null)
            throw new ListException("Left called on an empty queue");
            c = c.next;
        }
        if (c == null)
            throw new ListException("Left called on an empty queue");
        return c.data;
    }

    public T Right(int n)
    { Dequeue<T> c = left;
        for (int i = 0; i<n; i++)
        { if (c == null)
            throw new ListException("Right called on an empty queue");
            c = c.next;
        }
        if (c == null)
            throw new ListException("Right called on an empty queue");
        return c.data;
    }

    public int isEmpty()
    { Dequeue<T> c = left;
        int result = 0;
        while (c != null)
        { result++;
            c = c.next;
        }
        return result;
    }

    public String toString()
    { StringBuffer sb = new StringBuffer("<");
        Dequeue<T> c = left;
        while (c != null)
        { sb.append(c.data+",");
            c = c.next;
        }
        if (sb.length()>1)
            sb.setLength(sb.length()-1);   // remove trailing comma
        return(sb+">");
    }

    public void removeLeft() {
        Dequeue<T> c = left;
        if (c == null) {
            throw new ListException("removeLeft called on an empty queue");
        } else {
            left = c.next;
        }
    }

    public void removeRight() {
        Dequeue<T> c = left;
        if (c == null)
            throw new ListException("removeRight called on an empty queue");
        else
        if (c.next == null) left = c.next;
        else {
            while (c.next.next != null) {
                c = c.next;
            }
            c.next = null;
        }
    }
}