package com.nttdata.domain.contract;

import com.nttdata.domain.models.WalletDto;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface WalletRepository {
  Multi<WalletDto> list();

  Uni<WalletDto> findByNroWallet(WalletDto walletDto);

  Uni<WalletDto> addWallet(WalletDto walletDto);

  Uni<WalletDto> updateWallet(WalletDto walletDto);

  Uni<WalletDto> deleteWallet(WalletDto walletDto);

  Uni<WalletDto> depositWallet(WalletDto walletDto);
  Uni<WalletDto> withdrawalWallet(WalletDto walletDto);
}
