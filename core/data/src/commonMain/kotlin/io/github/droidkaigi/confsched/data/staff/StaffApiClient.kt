// FIXME: When the API is ready, remove Suppress annotation below.
@file:Suppress("UnusedPrivateProperty", "UnusedPrivateMember")

package io.github.droidkaigi.confsched.data.staff

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import io.github.droidkaigi.confsched.data.NetworkService
import io.github.droidkaigi.confsched.data.staff.response.StaffResponse
import io.github.droidkaigi.confsched.data.staff.response.StaffsResponse
import io.github.droidkaigi.confsched.model.Staff
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

internal interface StaffApi {
    /**
     * Gets staff information for the DroidKaigi 2024 event.
     *
     * @return [StaffsResponse]
     */
    @GET("/events/droidkaigi2024/staff")
    suspend fun getStaff(): StaffsResponse
}

public class DefaultStaffApiClient(
    private val networkService: NetworkService,
    ktorfit: Ktorfit,
) : StaffApiClient {

    private val staffApi = ktorfit.create<StaffApi>()

    public override suspend fun getStaff(): PersistentList<Staff> {
        return networkService {
            staffApi.getStaff()
        }.toStaffList()
    }
}

public interface StaffApiClient {
    public suspend fun getStaff(): PersistentList<Staff>
}

private fun StaffsResponse.toStaffList(): PersistentList<Staff> {
    return staff.map { it.toStaff() }.toPersistentList()
}

private fun StaffResponse.toStaff(): Staff {
    return Staff(
        id = id,
        username = username,
        iconUrl = iconUrl,
        profileUrl = profileUrl,
    )
}
