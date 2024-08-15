@import uk.anbu.spring.sample.petclinic.dto.VetDto
@import uk.anbu.spring.sample.petclinic.model.Vet.SpecialtyType
@import java.util.List

@param List<VetDto> listVets
@param int currentPage
@param int totalPages

<h2>Veterinarians</h2>

<table id="vets" class="table table-striped">
    <thead>
    <tr>
        <th>Name</th>
        <th>Specialties</th>
    </tr>
    </thead>
    <tbody>
    @for(VetDto vet : listVets)
        <tr>
            <td>${vet.getFirstName() + " " + vet.getLastName()}</td>
            <td>
                @for(SpecialtyType specialty : vet.getSpecialities())
                    ${specialty.toString()}
                @endfor
                @if(vet.getSpecialities().isEmpty())
                    none
                @endif
            </td>
        </tr>
    @endfor
    </tbody>
</table>

@if(totalPages > 1)
    <div>
        <span>Pages:</span>
        <span>[</span>
        @for(int i = 1; i <= totalPages; i++)
            @if(currentPage != i)
                <a href="/vets.html?page=${i}">${i}</a>
            @else
                <span>${i}</span>
            @endif
        @endfor
        <span>]&nbsp;</span>
        <span>
      @if(currentPage > 1)
          <a href="/vets.html?page=1" title="First" class="fa fa-fast-backward"></a>
      @else
          <span title="First" class="fa fa-fast-backward"></span>
      @endif
    </span>
        <span>
      @if(currentPage > 1)
          <a href="/vets.html?page=${currentPage - 1}" title="Previous" class="fa fa-step-backward"></a>
      @else
          <span title="Previous" class="fa fa-step-backward"></span>
      @endif
    </span>
        <span>
      @if(currentPage < totalPages)
          <a href="/vets.html?page=${currentPage + 1}" title="Next" class="fa fa-step-forward"></a>
      @else
          <span title="Next" class="fa fa-step-forward"></span>
      @endif
    </span>
        <span>
      @if(currentPage < totalPages)
          <a href="/vets.html?page=${totalPages}" title="Last" class="fa fa-fast-forward"></a>
      @else
          <span title="Last" class="fa fa-fast-forward"></span>
      @endif
    </span>
    </div>
@endif
