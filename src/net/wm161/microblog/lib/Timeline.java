/**
 *  This file is part of android-microblog
 *  Copyright (C) 2010 Trever Fischer <tdfischer@fedoraproject.org>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.wm161.microblog.lib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class Timeline implements Set<Status>, List<Status> {

	private ArrayList<Status> m_statuses;
	private long m_last;
	private OnNewStatusHandler m_handler = null;
	
	public void setOnNewStatusHandler(OnNewStatusHandler handler) {
		m_handler = handler;
	}

	public Timeline() {
		m_statuses = new ArrayList<Status>();
	}

	public Status[] getStatuses() {
		return (Status[]) m_statuses.toArray();
	}
	
	public long getLastId() {
		return m_last;
	}

	@Override
	public boolean add(Status status) {
		for(int i = 0;i < m_statuses.size();i++) {
			if (m_statuses.get(i).id() == status.id()) {
				return false;
			}
			if (status.id() > m_last)
				m_last = status.id();
			if (m_statuses.get(i).id() < status.id()) {
				m_statuses.add(i, status);
				if (m_handler != null)
					m_handler.onNewStatus(status);
				return true;
			}
		}
		m_statuses.add(status);
		if (m_handler != null)
			m_handler.onNewStatus(status);
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends Status> arg0) {
		m_statuses.removeAll(arg0);
		return m_statuses.addAll(arg0);
	}

	@Override
	public void clear() {
		m_statuses.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return m_statuses.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return m_statuses.containsAll(arg0);
	}

	@Override
	public Iterator<Status> iterator() {
		return m_statuses.iterator();
	}

	@Override
	public boolean remove(Object arg0) {
		return m_statuses.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return m_statuses.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return m_statuses.retainAll(arg0);
	}

	@Override
	public int size() {
		return m_statuses.size();
	}

	@Override
	public Object[] toArray() {
		return m_statuses.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return m_statuses.toArray(arg0);
	}

	@Override
	public boolean isEmpty() {
		return m_statuses.isEmpty();
	}

	@Override
	public void add(int location, Status s) {
		m_statuses.add(location, s);
	}

	@Override
	public boolean addAll(int location, Collection<? extends Status> arg1) {
		return m_statuses.addAll(location, arg1);
	}

	@Override
	public Status get(int location) {
		return m_statuses.get(location);
	}

	@Override
	public int indexOf(Object arg0) {
		return m_statuses.indexOf(arg0);
	}

	@Override
	public int lastIndexOf(Object arg0) {
		return m_statuses.lastIndexOf(arg0);
	}

	@Override
	public ListIterator<Status> listIterator() {
		return m_statuses.listIterator();
	}

	@Override
	public ListIterator<Status> listIterator(int arg0) {
		return m_statuses.listIterator(arg0);
	}

	@Override
	public Status remove(int arg0) {
		return m_statuses.remove(arg0);
	}

	@Override
	public Status set(int arg0, Status arg1) {
		return m_statuses.set(arg0, arg1);
	}

	@Override
	public List<Status> subList(int start, int end) {
		return m_statuses.subList(start, end);
	}
}
